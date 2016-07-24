const readDir = require('fs-readdir-recursive');
const path = require('path');
const webpack = require('webpack');
const failPlugin = require('webpack-fail-plugin');

const VERSION = require('./package.json').version;
const SOURCES_DIR = `${__dirname}/src/main`;
const ENTRY_REGEXP = new RegExp('.*?app\.[a-z]+$');

const isEntryPath = (filePath) => ENTRY_REGEXP.test(filePath);
const createEntry = (entries, path) => {

    var pathParts = path.split('/');
    var name = pathParts.slice(1, pathParts.length - 1).join('.');

    console.log(`Entry: ${name}`);
    entries[name] = `${SOURCES_DIR}/${path}`;

    return entries;
};

module.exports = {
    context: __dirname,
    entry: readDir(SOURCES_DIR)
        .filter(isEntryPath)
        .reduce(createEntry, {}),
    output: {
        path: path.resolve(__dirname, 'build'),
        filename: `[name]-${VERSION}.js`,
        publicPath: '/assets/',
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
        failPlugin,
        new webpack.DefinePlugin({VERSION})
    ]
};
