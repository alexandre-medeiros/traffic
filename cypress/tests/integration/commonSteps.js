const { Given, Then } = require("@badeball/cypress-cucumber-preprocessor");
//const { owner } = require("../../support/database/model/Owner");

let response = null;
let jsonData = null;

Given("I hit {string} to endpoint {string}", (method, url) => {
  cy.request({ method, url, failOnStatusCode: false }).then((result) => {
    response = result;
  });
});

Then("should return status code {string}", (code) => {
  expect(response.status).to.eq(+code);
});

Then("should return error message {string}", (msg) => {
  expect(response.body.title).to.eq(msg);
});

Then("should return error instance {string}", (instance) => {
  expect(response.body.instance).to.eq(instance);
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

Then("should remove the {string} existent in database with same id {string}", (tableName, id) => {
  thereIsNoRegistryInDatabase(tableName, id);
});

Then("there is no {string} in database with same id {string}", (tableName, id) => {
  thereIsNoRegistryInDatabase(tableName, id);
});

Given("I hit {string} to {string} with data:", function (method, url, data) {
  jsonData = JSON.parse(data);
  const body = jsonData;
  cy.request({ method, url, body, failOnStatusCode: false }).then((result) => {
    response = result;
  });
});

Then("the {string} is registred with success", function (tableName) {
  compareResponseWithDatabaseRegistry(response.body, tableName, response.body.id);
});

Then("the {string} with id {string} is updated with success", function (tableName, id) {
  const body = { id: Number(id), ...jsonData };
  compareResponseWithDatabaseRegistry(body, tableName, id);
});

function thereIsNoRegistryInDatabase(tableName, id) {
  const query = `SELECT * FROM ${tableName} WHERE id = ?`;
  const params = [id];
  cy.task("select", { query, params }).then((result) => {
    if (result.length == 0) {
      expect(result.length).to.equal(0);
    } else {
      throw new Error(`Data found in the database for ID: ${id}`);
    }
  });
}

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
