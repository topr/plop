module.exports = {
    context: __dirname,
    entry: './src/main/javascript/entry.js',
    output: {
        path: './build/js',
        filename: 'plop-web.bundle.js'
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
    }
};
