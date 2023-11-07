const { join } = require("path");
const root_path = join(__dirname, "../../../");
const pathResults = `${root_path}/cypress/tests/results/`;

function getResultPath() {
  return pathResults;
}

function getRootPath() {
  return root_path;
}

module.exports = {
  getResultPath,
  getRootPath,
};
