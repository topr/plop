module.exports = {
    context: __dirname,
    entry: {
        'amd-module.example': './src/main/javascript/example/amd-module/app.js'
    },
    output: {
        path: './build',
        filename: '[name].js'
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
