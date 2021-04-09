## 1、springboot集成redis

**pom依赖添加**

```xml
<!--redis依赖-->
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-data-redis</artifactId>
    </dependency>
    <!--commons-pool2 对象池依赖-->
    <dependency>
      <groupId>org.apache.commons</groupId>
      <artifactId>commons-pool2</artifactId>
    </dependency>
```

**yml配置**

```yaml
spring:
  redis:
    # 超时时间
    timeout: 10000ms
    host: 127.0.0.1
    port: 6379
    database: 0
    lettuce:
      pool:
        # 最大连接数，默认8
        max-active: 1024
        # 最大连接阻塞等待时间 默认-1
        max-wait: 10000ms
        # 最大空闲连接
        max-idle: 200
        # 最小空闲连接
        min-idle: 5
```

**配置类**

```java
@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate redisTemplate = new RedisTemplate();
        // String类型， key序列器
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        // String类型， value序列器
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        // Hash类型， key序列器
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        // Hash类型， value序列器
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }

}
```

**使用范例**

```java
	@Autowired
    private RedisTemplate redisTemplate;

    public List<Menu> getMenuListByAdminId() {
        Admin admin = (Admin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer adminId = admin.getId();
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        // 从redis获取菜单
        List<Menu> menus = (List<Menu>) valueOperations.get("menu_" + adminId);
        // 如果为空，去数据库获取
        if (CollectionUtils.isEmpty(menus)) {
            menus = menuMapper.getMenuListByAdminId(adminId);
            // 将数据设置到redis中
            valueOperations.set("menu_" + adminId,menus);
        }
        return menus;
    }
```

## 2、springsecurity + jwttoken实现登录以及权限控制

##### pom依赖

```xml
    <!--security依赖-->
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    <!--JWT依赖-->
    <dependency>
      <groupId>io.jsonwebtoken</groupId>
      <artifactId>jjwt</artifactId>
      <version>0.9.1</version>
    </dependency>
```

##### yml配置

```yaml
# JWT相关配置
jwt:
  tokenHeader: Authorization
  secret: yeb-secret
  expiration: 604800  # 30*60*24*7
  tokenHead: Bearer
```

##### JwtTokenUtil token工具

```java
@Configuration
public class JwtTokenUtil {
    private static final String CLAIM_KEY_USERNAME = "sub";
    private static final String CLAIM_KEY_CREATED = "created";
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * 根据用户信息生成token
     * @param userDetails
     * @return
     */
    public String generateToken(UserDetails userDetails){
        Map<String,Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME,userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED,new Date());
        return generateToken(claims);
    }

    /**
     * 从token中获取登录名
     * @param token
     * @return
     */
    public String getUserNameFromToken(String token) {
        String username;
        try {
            Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    /**
     * 验证token是否有效
     * @param token
     * @param userDetails
     * @return
     */
    public boolean validateToken(String token,UserDetails userDetails) {
        String username = getUserNameFromToken(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * 判断token是否可以被刷新
     * @param token
     * @return
     */
    public boolean canRefresh(String token) {
        return !isTokenExpired(token);
    }

    /**
     * 在过期时间内刷新token
     * @param token
     * @return
     */
    public String refreshToken(String token) {
        Claims claims  = getClaimsFromToken(token);
        claims.put(CLAIM_KEY_CREATED,new Date());
        return generateToken(claims);
    }

    /**
     * 判断token是否过期
     * @param token
     * @return
     */
    private boolean isTokenExpired(String token) {
        Date expireDate = getExpiredDateFromToken(token);
        return expireDate.before(new Date());   // 当过期时间小于或者等于当前时间时，返回true
    }

    /**
     * 从token中获取过期时间
     * @param token
     * @return
     */
    private Date getExpiredDateFromToken(String token) {
        Claims  claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }

    /**
     * 从token中获取荷载
     * @param token
     * @return
     */
    private Claims getClaimsFromToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return claims;
    }

    /**
     * 根据荷载生成JWTtoken
     * @param claims
     * @return
     */
    private String generateToken(Map<String,Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS256,secret)
                .compact();
    }

    /**
     * 生成token失效时间
     * @return
     */
    private Date generateExpirationDate(){
        return new Date(System.currentTimeMillis()+expiration*1000);
    }
}
```

##### 以下是各种配置

###### 认证入口配置

```java
/**
 * 当未登录或者token失效时访问接口时，自定义的返回接口
 * @author LiuSu
 * @create 2021/3/12 19:21
 */
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        R r = R.error("未登录，请登录");
        r.setCode(401);
        out.write(new ObjectMapper().writeValueAsString(r));
        out.flush();
        out.close();
    }
}
```

###### **授权失败配置**

```java
**
 * @author LiuSu
 * @create 2021/3/12 19:26
 */
@Component
public class RestfulAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        R r = R.error("权限不足，请联系管理员");
        r.setCode(403);
        out.write(new ObjectMapper().writeValueAsString(r));
        out.flush();
        out.close();
    }
}

```

###### **根据请求url设置角色配置**

```java
/**
 * 权限控制
 * 根据请求url分析请求所需的角色
 * @author LiuSu
 * @create 2021/3/17 13:26
 */
@Component
public class CustomFilter implements FilterInvocationSecurityMetadataSource {

    @Autowired
    private MenuService menuService;

    AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        // 获取请求的url
        String requestUrl = ((FilterInvocation) o).getRequestUrl();
        List<Menu> menus = menuService.getMenuListWithRole();
        for (Menu menu : menus) {
            // 判断请求url与菜单角色是否匹配
            if (antPathMatcher.match(menu.getUrl(),requestUrl)) {
                String[] str = menu.getRoles().stream().map(Role::getName).toArray(String[]::new);
                return SecurityConfig.createList(str);
            }
        }
        // 没匹配的url默认登录即可访问
        return SecurityConfig.createList("ROLE_LOGIN");
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return false;
    }
}
```

###### **判断登录用户是否与请求url所需角色匹配配置**

```java
/**
 * 权限控制
 * 判断用户角色
 * @author LiuSu
 * @create 2021/3/17 13:46
 */
@Component
public class CustomUrlDecisionManager implements AccessDecisionManager {
    @Override
    public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        for (ConfigAttribute configAttribute : configAttributes){
            // 当前url所需角色
            String needRole = configAttribute.getAttribute();
            // 判断角色是否登录即可访问的角色，此角色在CustomFilter中设置
            if ("ROLE_LOGIN".equals(needRole)){
                // 判断是否登录
                if(authentication instanceof AnonymousAuthenticationToken) {
                    throw new AccessDeniedException("尚未登录，请登录");
                }else {
                    return;
                }
            }
            // 判断用户角色是否为url所需角色
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            for (GrantedAuthority authority : authorities){
                if (authority.getAuthority().equals(needRole)){
                    return;
                }
            }
        }
        throw new AccessDeniedException("权限不足，请联系管理员");
    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return false;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return false;
    }
}
```

###### 用户登录认证配置

```java
/**
 * JWT登录授权过滤器
 * @author LiuSu
 * @create 2021/3/12 18:59
 */
public class AuthenticationFilter extends OncePerRequestFilter {

    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(tokenHeader);
        // 判断是否存在token
        if (authHeader!=null && authHeader.startsWith(tokenHead)) {
            String token = authHeader.substring(tokenHead.length());
            String username = jwtTokenUtil.getUserNameFromToken(token);
            // token存在用户名 但 未登录
            if (username!=null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // 登录
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                // 验证token是否有效，重新设置用户对象
                if (jwtTokenUtil.validateToken(token,userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken = new
                            UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }
        filterChain.doFilter(request,response);
    }
}
```

###### 最后是总的SecurityConfig配置

```java

/**
 * @author LiuSu
 * @create 2021/3/12 18:49
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AdminService adminService;
    @Autowired
    private RestAuthenticationEntryPoint authenticationEntryPoint;
    @Autowired
    private RestfulAccessDeniedHandler accessDeniedHandler;
    @Autowired
    private CustomUrlDecisionManager customUrlDecisionManager;
    @Autowired
    private CustomFilter customFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                "/login","/logout",
                "/css/**","/js/**","/index.html","/img/**","favicon.ico",
                "/doc.html",
                "/webjars/**",
                "/swagger-resources/**",
                "/v2/api-docs/**",
                "/captcha" //验证码接口
        );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 使用JWT不需要csrf
        http.csrf()
                .disable()
                // 基于token，不需要session
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // 所有接口都需要认证
                .anyRequest()
                .authenticated()
                // 动态权限配置
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                        o.setAccessDecisionManager(customUrlDecisionManager);
                        o.setSecurityMetadataSource(customFilter);
                        return o;
                    }
                })
                .and()
                // 禁用缓存
                .headers()
                .cacheControl();
        // 添加jwt 登录授权过滤器
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        // 添加自定义未授权和未登录结果返回
        http.exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler);
    }

    @Override
    @Bean
    public UserDetailsService userDetailsService(){
        return username -> {
            Admin admin = adminService.getAdminByUserName(username);
            if (admin != null) {
                admin.setRoles(adminService.getRolesByAdminId(admin.getId()));
                return admin;
            }
            throw new UsernameNotFoundException("用户名或密码不正确");
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationFilter jwtAuthenticationFilter(){
        return new AuthenticationFilter();
    }
}

```

###### 还有一个用户登录的Service

```java
@Override
    public R login(AdminLoginVO adminLoginVO, HttpServletRequest request) {
        // 判断验证是否正确
        String code = (String) request.getSession().getAttribute("captcha");
        if (code!=null && !code.equals(adminLoginVO.getCode())) {
            return R.error("验证码错误！");
        }
        // 判断用户是否存在以及密码是否正确
        UserDetails userDetails = userDetailsService.loadUserByUsername(adminLoginVO.getUsername());
        if (userDetails == null || !passwordEncoder.matches(adminLoginVO.getPassword(),userDetails.getPassword())){
            return R.error("用户名或者密码错误！");
        }
        if (!userDetails.isEnabled()) {
            return R.error("账号被禁用，请联系管理员！");
        }

        // 更新Security登录用户对象信息
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        // 生成Token
        String token = jwtTokenUtil.generateToken(userDetails);
        Map<String,Object> map = new HashMap<>();
        map.put("token",token);
        map.put("tokenHead",tokenHead);
        return R.success(map);
    }
```

## 3、存储过程

MySQL 5.0版本开始支持存储过程。

存储过程（Stored Procedure）是一种在数据库中存储复杂程序，以便外部程序调用的一种数据库对象。

存储过程是为了完成特定功能的SQL语句集，经编译创建并保存在数据库中，用户可通过存储过程的名字并给定参数（需要时）来调用执行。

存储过程思想上很简单，就是数据库SQL语言层面的代码封装与重用。

通俗来讲：存储过程其实就是能完成一定操作的一组SQL语句。

**优点：**

- 存储过程可封装，并隐藏复杂的商业逻辑。

- 存储过程可以回传值，并可以接收参数。
- 存储过程无法使用SELECT指令来运行，因为它是子程序，与查看表，数据表或用户定义函数不同。
- 存储过程可以用在数据检验，强制实行商业逻辑等上。

**缺点：**

- 存储过程，往往定制化于特定的数据库上，因为支持的编程语言不同。当切换到其他厂商的数据库系统时，需要重写原有的存储过程。
- 存储过程的性能调校与撰写，受限于各种数据库系统。

###### 1.创建存储过程

```mysql
CREATE
	[DEFINER = {user | CURRENT_USER}]
	PROCEDURE sp_name ([proc_parameter[,...]])
		[characteristic ...] routine_body
		
proc_parameter:
	[IN | OUT | INOUT] param_name type
	
characteristic:
	COMMENT 'string'
	| LANGUAGE SQL
	| [NOT] DETERMINISTIC
	| {CONTAINS SQL | NO SQL | READS SQL DATA | MODIFIES SQL DATA}
	| SQL SECURITY {DEFINER | INVOKER}

routine_body:
	Valid SQL routine statement

[begin_label:] BEGIN
	[statement_list]
	...

END [end_label]
```

