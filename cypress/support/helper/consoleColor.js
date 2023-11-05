﻿const clc = require("cli-color");
const process = require("process");
const error = clc.red.bold;
const warn = clc.yellow;
const notice = clc.greenBright.italic;

module.exports = {
  ERROR,
  FAIL,
  WARN,
  NOTICE,
};

function FAIL(msg) {
  console.log(error(msg));
}

function ERROR(msg, query, formatView) {
  if (formatView) {
    console.log();
    console.log(error("ERROR - "), error(msg));
    console.log(warn(formatView(query)));
    process.exit(1);
  } else {
    ERROR2(msg, query);
  }
}

function WARN(msg) {
  console.log(warn(msg));
}

function NOTICE(msg) {
  console.log(notice(msg));
}
