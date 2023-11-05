const { getRootPath } = require("../file/pathConfigService");
const { NOTICE, WARN } = require("../consoleColor");
const { getFileIfExist } = require("../file/fileHelper");

var maxFileNameLength = 0;
var results = [];

const header = () => {
  const spaces = " ".repeat(maxFileNameLength);
  return `     Spec ${spaces}   Tests  Passing  Failing  Pending  Skipped`;
};

const headerL = () => {
  const spaces = "─".repeat(maxFileNameLength);
  return `┌─────${spaces}────────────────────────────────────────────────┐`;
};

const footerL = () => {
  const spaces = "─".repeat(maxFileNameLength);
  return `└─────${spaces}────────────────────────────────────────────────┘`;
};

const separator = () => {
  const spaces = "─".repeat(maxFileNameLength);
  return `├─────${spaces}────────────────────────────────────────────────┤`;
};

const footer = (data) => {
  const minus = "6     2      4      0      0".length;
  const space = " ".repeat(maxFileNameLength - minus);
  return `  ✔  ${data.stats.passes} of ${
    data.stats.tests
  } passed (${data.stats.passPercent.toFixed(2)}%) ${space}\t${formatTime(
    data.stats.duration
  )}\t${data.stats.testsRegistered}\t${data.stats.passes}\t${
    data.stats.failures
  }\t${data.stats.pending}\t${data.stats.skipped}`;
};

function formatTime(milliseconds) {
  const minutes = Math.floor(milliseconds / 60000);
  const seconds = ((milliseconds % 60000) / 1000).toFixed(0);
  return `${minutes}:${seconds < 10 ? "0" : ""}${seconds}`;
}

function setTestResults(data) {
  for (const result of data.results) {
    const fileName = result.file;
    const testStats = result.suites[0].tests;
    const totalTime = testStats.reduce(
      (total, test) => total + (test.duration || 0),
      0
    );
    const passingTests = testStats.filter(
      (test) => test.state === "passed"
    ).length;
    const failingTests = testStats.filter(
      (test) => test.state === "failed"
    ).length;
    const pendingTests = testStats.filter(
      (test) => test.state === "pending"
    ).length;
    const skippedTests = testStats.filter(
      (test) => test.state === "skipped"
    ).length;

    if (fileName.length > maxFileNameLength) {
      maxFileNameLength = fileName.length;
    }

    results.push(
      `│ ✖  ${fileName}\t${formatTime(totalTime)}\t${
        testStats.length
      }\t${passingTests}\t${failingTests}\t${pendingTests}\t${skippedTests}`
    );
  }
}

async function main() {
  const fullPath = `${getRootPath()}/mochawesome.json`;
  const data = JSON.parse(getFileIfExist(fullPath));
  setTestResults(data);

  console.log();
  NOTICE(header());
  WARN(headerL());

  for (index = 0; index < results.length; index++) {
    WARN(results[index]);
    index < results.length - 1 ? WARN(separator()) : null;
  }

  WARN(footerL());
  NOTICE(footer(data));
  console.log();
}

main();
/*
                    Spec                                              Tests  Passing  Failing  Pending  Skipped
                    ┌────────────────────────────────────────────────────────────────────────────────────────────────┐
                    │ ✖  owner/features/owner.feature             574ms        3        1        2        -        - │
                    ├────────────────────────────────────────────────────────────────────────────────────────────────┤
                    │ ✖  vehicle/features/vehicle.feature         513ms        3        1        2        -        - │
                    └────────────────────────────────────────────────────────────────────────────────────────────────┘
                      ✖  2 of 2 failed (100%)                     00:01        6        2        4        -        -

*/