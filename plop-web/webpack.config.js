module.exports = {
    context: __dirname,
    entry: './src/main/javascript/app.js',
    output: {
        path: './build',
        filename: 'plop-web.js'
    },
    devtool: 'source-map',
    module: {
        loaders: [
            {
                test: /\.html$/,
                loader: 'raw'
            },
            {
                test: /\.css$/,
                loaders: ['style', 'css']
            },
            {
                test: /\.js$/,
                exclude: /node_modules/,
                loader: 'babel-loader',
            }
        ]
    },
    plugins: [
        require('webpack-fail-plugin')
    ]
};
