const { Given, Then } = require("@badeball/cypress-cucumber-preprocessor");
//const { owner } = require("../../support/database/model/Owner");

let response = null;
let jsonData = null;

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
  cy.task("select", { query }).then((result) => {
    expect(data).to.deep.equal(result);
  });
});

Then("should return the {string} existent in database with same id {string}", (tableName, id) => {
  compareResponseWithDatabaseRegistry(response.body, tableName, id);
});

Given("I hit {string} to {string} with data:", function (method, url, data) {
  jsonData = JSON.parse(data);
  cy.request(method, url, jsonData).then((result) => {
    response = result;
  });
});

Then("the {string} is registred with success", function (tableName) {
  compareResponseWithDatabaseRegistry(response.body, tableName, response.body.id);
});

Then("the {string} with id {string} is updated with success", function (tableName, id) {
  const body = { id: Number(id), ...jsonData };
  cy.log(JSON.stringify(body));
  compareResponseWithDatabaseRegistry(body, tableName, id);
});

function compareResponseWithDatabaseRegistry(response, tableName, id) {
  const query = `SELECT * FROM ${tableName} WHERE id = ?`;
  const params = [id];
  cy.task("select", { query, params }).then((result) => {
    if (result.length > 0) {
      expect(response).to.deep.equal(result[0]);
    } else {
      throw new Error(`No data found in the database for ID: ${id}`);
    }
  });
}
