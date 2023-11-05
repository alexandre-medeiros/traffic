const fs = require("fs");

function getFileIfExist(file) {
  const buffer = fs.readFileSync(file);

  if (!buffer) {
    ERROR("Não existe resultados de teste para serem processados");
  }

  return buffer;
}

module.exports = {
  getFileIfExist,
};
