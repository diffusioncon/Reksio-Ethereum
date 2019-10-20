import React from 'react';
import styled from 'styled-components';
import Select from '../../../components/Select';
import { useEnterKey } from '../../../utils';
import { StepProps } from '..';

const options = [{ value: 'S3', label: 'S3' }];

const StorageStep: React.FC<StepProps> = ({ onStepCompleted }) => {
  useEnterKey(onStepCompleted);
  return (
    <>
      <StyledHeader>Choose your data storage</StyledHeader>
      <Select options={options} />
    </>
  );
};

const StyledHeader = styled.h2`
  margin-bottom: 66px;
`;

export default StorageStep;
