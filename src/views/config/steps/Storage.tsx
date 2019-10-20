import React from 'react';
import Select from 'react-select';
import styled from 'styled-components';

const options = [{ value: 'S3', label: 'S3' }];

const StorageStep: React.FC = () => {
  return (
    <>
      <h1>Choose your data storage</h1>
      <StyledSelect options={options} />
    </>
  );
};

const StyledSelect = styled(Select)`
  width: 300px;
  margin-top: 30px;
`;

export default StorageStep;
