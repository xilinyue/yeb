module.exports = {
    devServer: {
        proxy: {
            '/': {
                target: 'http://localhost:9090/',
                ws: true,
                changeOrigin: true
            }
        }
    }
}