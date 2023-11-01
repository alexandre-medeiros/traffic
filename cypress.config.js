const { defineConfig } = require("cypress");

const preprocessor = require("@badeball/cypress-cucumber-preprocessor");
const browserify = require("@badeball/cypress-cucumber-preprocessor/browserify");

async function setupNodeEvents(on, config) {
  await preprocessor.addCucumberPreprocessorPlugin(on, config);
  on("file:preprocessor", browserify.default(config));
  return config;
} 

module.exports = defineConfig({
  e2e: {
    setupNodeEvents,
    specPattern: "cypress/tests/**/*.feature",
    baseUrl: `http://${process.env.HOST_API}:${process.env.HOST_PORT_API}/`,
    },
});
