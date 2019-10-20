pragma solidity ^0.5.0;

contract FileNotary {
    struct FileInfo {
        bytes32 hash;
        uint256 timestamp;
        bool set;
    }

    mapping(address => mapping(string => FileInfo)) addressFiles;

    function getFileHash(address owner, string memory fileName) public view returns (bytes32) {
      return addressFiles[owner][fileName].hash;
    }

    function getOwnFileHash(string memory fileName) public view returns (bytes32) {
      return addressFiles[msg.sender][fileName].hash;
    }

    function setFileHash(string memory fileName, bytes32 hash) public {
      require(addressFiles[msg.sender][fileName].set == false, "You cannot mutate already set hash");
      FileInfo memory fileInfo;
      fileInfo.hash = hash;
      // solium-disable-next-line security/no-block-members
      fileInfo.timestamp = block.timestamp;
      fileInfo.set = true;
      addressFiles[msg.sender][fileName] = fileInfo;
    }
}
