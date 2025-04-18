const webpackMerge = require('webpack-merge').merge;
const BrowserSyncPlugin = require('browser-sync-webpack-plugin');
const SimpleProgressWebpackPlugin = require('simple-progress-webpack-plugin');
const WebpackNotifierPlugin = require('webpack-notifier');
const path = require('path');
const sass = require('sass');
const postcssRTLCSS = require('postcss-rtlcss');

const utils = require('./utils.js');
const commonConfig = require('./webpack.common.js');

const ENV = 'development';

module.exports = async options =>
  webpackMerge(await commonConfig({ env: ENV }), {
    mode: ENV, // 🔧 وضع التطوير
    devtool: 'cheap-module-source-map',

    entry: ['./src/main/webapp/app/index'], // ✅ تأكد أن هذا الملف موجود

    output: {
      path: utils.root('target/classes/static/'),
      filename: '[name].[contenthash:8].js',
      chunkFilename: '[name].[chunkhash:8].chunk.js',
    },

    optimization: {
      moduleIds: 'named',
    },

    module: {
      rules: [
        {
          test: /\.(sa|sc|c)ss$/,
          use: [
            'style-loader',
            {
              loader: 'css-loader',
              options: { url: false },
            },
            {
              loader: 'postcss-loader',
              options: {
                postcssOptions: {
                  plugins: [postcssRTLCSS()],
                },
              },
            },
            {
              loader: 'sass-loader',
              options: { implementation: sass },
            },
          ],
        },
      ],
    },

    devServer: {
      hot: true,
      static: {
        directory: utils.root('target/classes/static/'), // ⚠️ تأكد من وجود هذا المسار
      },
      port: 9060,
      proxy: [
        {
          context: ['/api', '/services', '/management', '/v3/api-docs', '/h2-console'],
          target: `http${options.tls ? 's' : ''}://localhost:8080`,
          secure: false,
          changeOrigin: options.tls,
        },
      ],
      historyApiFallback: true,
    },

    stats: process.env.JHI_DISABLE_WEBPACK_LOGS ? 'none' : options.stats,

    plugins: [
      process.env.JHI_DISABLE_WEBPACK_LOGS
        ? null
        : new SimpleProgressWebpackPlugin({
          format: options.stats === 'minimal' ? 'compact' : 'expanded',
        }),

      new BrowserSyncPlugin(
        {
          https: options.tls,
          host: 'localhost',
          port: 9000,
          proxy: {
            target: `http${options.tls ? 's' : ''}://localhost:${options.watch ? '8080' : '9060'}`,
            ws: true,
            proxyOptions: {
              changeOrigin: false,
            },
          },
          socket: {
            clients: {
              heartbeatTimeout: 60000,
            },
          },
        },
        {
          reload: false,
        },
      ),

      new WebpackNotifierPlugin({
        title: 'Smart Vax App',
        contentImage: path.join(__dirname, 'logo-jhipster.png'),
      }),
    ].filter(Boolean),
  });
