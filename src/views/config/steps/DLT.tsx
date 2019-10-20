import React from 'react';
import Select from 'react-select';
import styled from 'styled-components';

const options = [{ value: 'ethereum', label: 'Ethereum' }];

const DLTStep: React.FC = () => {
  return (
    <>
      <h1>Choose your DLT</h1>
      <StyledSelect options={options} />
    </>
  );
};

const StyledSelect = styled(Select)`
  width: 300px;
  margin-top: 30px;
`;

export default DLTStep;
