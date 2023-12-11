const { getRootPath } = require("../file/pathConfigService");
const { ERROR, FAIL, NOTICE, WARN } = require("../../consoleColor");
const { getFileIfExist } = require("../file/fileHelper");

var maxFileNameLength = 0;
var results = [];

const header = () => {
  const spaces = " ".repeat(maxFileNameLength);
  return `     Spec ${spaces}Tests  Passing  Failing  Pending  Skipped`;
};

const headerL = () => {
  const spaces = "─".repeat(maxFileNameLength);
  return `┌─────${spaces}───────────────────────────────────────────┐`;
};

const footerL = () => {
  const spaces = "─".repeat(maxFileNameLength);
  return `└─────${spaces}───────────────────────────────────────────┘`;
};

const separator = () => {
  const spaces = "─".repeat(maxFileNameLength);
  return `├─────${spaces}───────────────────────────────────────────┤`;
};

const footer = (data) => {
  const minus = "6   2    4    0    0".length;
  const quantitY = maxFileNameLength < minus ? 1 : maxFileNameLength - minus;
  const space = " ".repeat(quantitY);
  return `  ✔  ${data.stats.passes} of ${data.stats.tests} passed    ${space}\t ${data.stats.testsRegistered}\t ${data.stats.passes}\t ${data.stats.failures}\t ${data.stats.pending}\t ${data.stats.skipped}`;
};

function formatTime(milliseconds) {
  const minutes = Math.floor(milliseconds / 60000);
  const seconds = ((milliseconds % 60000) / 1000).toFixed(0);
  return `${minutes}:${seconds < 10 ? "0" : ""}${seconds}`;
}

function setTestResults(data) {
  for (const result of data.results) {
    const splitPath = result.file.split("/");

    if (splitPath.length === 0) {
      ERROR("Erro ao ler o arquivo mochawesome.json");
    }

    const fileName = splitPath[splitPath.length - 1];
    const testStats = result.suites[0].tests;
    const totalTime = testStats.reduce((total, test) => total + (test.duration || 0), 0);
    const passingTests = testStats.filter((test) => test.state === "passed").length;
    const failingTests = testStats.filter((test) => test.state === "failed").length;
    const pendingTests = testStats.filter((test) => test.state === "pending").length;
    const skippedTests = testStats.filter((test) => test.state === "skipped").length;

    if (fileName.length > maxFileNameLength) {
      maxFileNameLength = fileName.length;
    }

    results.push(
      `│ ✖  ${fileName}\t ${testStats.length}\t ${passingTests}\t ${failingTests}\t ${pendingTests}\t ${skippedTests}`
    );
  }
}

async function main() {
  const fullPath = `${getRootPath()}/mochawesome.json`;
  const data = JSON.parse(getFileIfExist(fullPath));
  setTestResults(data);

  console.log();
  console.log();
  NOTICE(header());
  WARN(headerL());

  for (index = 0; index < results.length; index++) {
    WARN(results[index]);
    index < results.length - 1 ? WARN(separator()) : null;
  }

  WARN(footerL());

  if (data.stats.passPercent === 100.0) {
    NOTICE(footer(data));
  } else {
    FAIL(footer(data));
  }

  console.log();
  console.log();
}

main();
