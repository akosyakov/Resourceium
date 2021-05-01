export default {
  // Global page headers (https://go.nuxtjs.dev/config-head)
  head: {
    title: 'Resourceium',
    meta: [
      { charset: 'utf-8' },
      { name: 'viewport', content: 'width=device-width, initial-scale=1' },
      { hid: 'description', name: 'description', content: '' },
    ],
    link: [{ rel: 'icon', type: 'image/x-icon', href: '/favicon.png' }],
  },

  // Global CSS (https://go.nuxtjs.dev/config-css)
  css: ['~/assets/css/global.scss'],

  // Plugins to run before rendering page (https://go.nuxtjs.dev/config-plugins)
  plugins: [
    '~/plugins/ignisnents.js',
    '~/plugins/alerts.js',
    '~/plugins/uploads.js',
  ],

  // Auto import components (https://go.nuxtjs.dev/config-components)
  components: true,

  // Modules for dev and build (recommended) (https://go.nuxtjs.dev/config-modules)
  buildModules: [
    // https://go.nuxtjs.dev/eslint
    '@nuxtjs/eslint-module',
    // https://go.nuxtjs.dev/stylelint
    '@nuxtjs/stylelint-module',
    // https://go.nuxtjs.dev/tailwindcss
    '@nuxtjs/tailwindcss',
  ],

  // Modules (https://go.nuxtjs.dev/config-modules)
  modules: [
    // https://go.nuxtjs.dev/pwa
    '@nuxtjs/pwa',
    // https://axios.nuxtjs.org
    '@nuxtjs/axios',
    // https://google-fonts.nuxtjs.org/
    '@nuxtjs/google-fonts',
  ],

  // Build Configuration (https://go.nuxtjs.dev/config-build)
  build: {},

  // Added later
  telemetry: false,
  watchers: {
    webpack: {
      aggregateTimeout: 300,
      poll: 1000,
    },
  },
  target: 'server',
  googleFonts: {
    families: {
      'Original Surfer': true,
      Poppins: true,
    },
    display: 'swap',
  },
  generate: {
    subFolders: false,
  },
  env: {
    baseUrl: process.env.API_URL || 'http://localhost:8000',
  },
  pwa: {
    icon: {
      fileName: 'icon.png',
    },
    manifest: {
      name: 'Resourceium',
      short_name: 'Resourceium',
      description: 'Store all your precious resources at one place',
    },
  },
}
