const { defineConfig } = require("cypress");
const { query } = require("./helper/database/infra/data-source");
const preprocessor = require("@badeball/cypress-cucumber-preprocessor");
const browserify = require("@badeball/cypress-cucumber-preprocessor/browserify");

async function setupNodeEvents(on, config) {
  console.log(
    `baseUrl: http://${process.env.HOST_API}:${process.env.HOST_PORT_API}/`
  );
  await preprocessor.addCucumberPreprocessorPlugin(on, config);
  on("file:preprocessor", browserify.default(config));

  on("task", {
    select: (values) => {
      return query(values);
    },
  });

  return config;
}

module.exports = defineConfig({
  e2e: {
    setupNodeEvents,
    specPattern: "cypress/tests/**/*.feature",
    baseUrl: `http://${process.env.HOST_API}:${process.env.HOST_PORT_API}/`,
    screenshotOnRunFailure: false,
  },
  reporter: "mochawesome",
  reporterOptions: {
    reportDir: "cypress/tests/results",
    overwrite: false,
    html: false,
    json: true,
  },
});
