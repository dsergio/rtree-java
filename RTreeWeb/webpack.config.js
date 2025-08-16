// client/webpack.config.js
const path = require('path');
const { VueLoaderPlugin } = require('vue-loader');

module.exports = {
  mode: 'development',
  entry: './src/main.ts',
  watch: false,
  output: {
    filename: 'bundle.js',
    path: path.resolve(__dirname, 'dist'),
    publicPath: '/dist/',
  },
  resolve: {
    extensions: ['.ts', '.js', '.vue', '.json'],
    alias: { 
        '@': path.resolve(__dirname, 'src'),
        vue: 'vue/dist/vue.esm-bundler.js' },
  },
  module: {
    rules: [
      { test: /\.vue$/, loader: 'vue-loader' },
      { test: /\.ts$/, loader: 'ts-loader', options: { appendTsSuffixTo: [/\.vue$/] } },
      { test: /\.css$/, use: ['style-loader', 'css-loader'] },
    ],
  },
  plugins: [
    new VueLoaderPlugin(),
  ],
  devServer: {
    static: path.join(__dirname, '/'),
    port: 3000,
    // proxy: [
    //   {
    //     // proxy all /api requests
    //     context: ['/api'],
    //     target: 'http://localhost:8080',
    //     changeOrigin: true,   // useful for virtual hosted sites
    //     secure: false,        // if your backend is self-signed HTTPS
    //     // pathRewrite: { '^/api': '' }, // if you need to strip the /api prefix
    //   }
    // ],
    open: true,
  },
};
