import React, { useState } from 'react';
import Select from '../../../components/Select';
import styled from 'styled-components';
import axios from 'axios';
import { StepProps } from '..';
import { useEnterKey } from '../../../utils';
import { Option } from '../../../components/Select';

const options = [{ value: 'ETHEREUM', label: 'Ethereum' }];

const DLTStep: React.FC<StepProps> = ({ onStepCompleted }) => {
  const [isLoading, setIsLoading] = useState(false);
  const [dltValue, setDltValue] = useState<Option | null>(null);

  useEnterKey(async () => {
    if (!isLoading) {
      setIsLoading(true);
      try {
        const data = {
          dlt: dltValue && dltValue.value,
        };
        await axios.put('/api/configs/dlt', data);
        onStepCompleted();
      } catch (e) {
        console.error(e);
      }
    }
  });

  return (
    <>
      <h2>Choose your DLT</h2>
      <StyledSelect options={options} value={dltValue} onInput={setDltValue} />
    </>
  );
};

const StyledSelect = styled(Select)`
  margin-top: 30px;
`;

export default DLTStep;
