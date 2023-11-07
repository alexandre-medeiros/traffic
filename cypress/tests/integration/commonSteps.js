const { Given, Then } = require("@badeball/cypress-cucumber-preprocessor");
//const { owner } = require("../../support/database/model/Owner");

let response = null;

Given("I hit {string} to endpoint {string}", (method, url) => {
  cy.request(method, url).then((result) => {
    response = result;
  });
});

Then("should return status code {string}", (code) => {
  expect(response.status).to.eq(+code);
});

Then("should return all {string}", (tableName) => {
  const data = response.body;
  const query = `SELECT * FROM ${tableName}`;
  cy.task("select", { query }).then((resul) => {
    expect(data).to.deep.equal(resul);
  });
});
