const {createProxyMiddleware} = require('http-proxy-middleware');

module.exports = function (app) {
    app.use(
        '/api',
        createProxyMiddleware({
            target: process.env.API_URL,
            changeOrigin: true,
            onError: function (err, req, res, target) {
                res.status(503).send(
                    {
                        "status": 503,
                        "message": "Сервис недоступен"
                    }
                );
            }
        })
    );
};
