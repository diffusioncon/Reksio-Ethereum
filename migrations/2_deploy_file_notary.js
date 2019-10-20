const FileNotary = artifacts.require("FileNotary");
const fs = require("fs");

const deployedDir = "deployed";
const contractFile = "fileNotary.json";

module.exports = async function(deployer) {
  await deployer.deploy(FileNotary);
  const instance = await FileNotary.deployed();
  const contract = { address: instance.address, abi: instance.abi };
  fs.mkdirSync(deployedDir);
  fs.writeFileSync(`${deployedDir}/${contractFile}`, JSON.stringify(contract));
};
