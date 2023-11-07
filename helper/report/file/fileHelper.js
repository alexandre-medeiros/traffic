const fs = require("fs");
const { ERROR } = require("../../consoleColor");

function getFileIfExist(file) {
  if (!fs.existsSync(file)) {
    ERROR("Não existe resultados de teste para serem processados");
  }

  return fs.readFileSync(file);
}

module.exports = {
  getFileIfExist,
};
