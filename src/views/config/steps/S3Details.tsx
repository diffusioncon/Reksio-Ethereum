import React, { useState } from 'react';
import styled from 'styled-components';
import Select, { Option } from '../../../components/Select';
import { useEnterKey } from '../../../utils';
import { StepProps } from '..';
import axios from 'axios';
import { REGIONS } from './regions';
import { CircularProgress } from '@material-ui/core';

const regionOptions = REGIONS;

const S3Details: React.FC<StepProps> = ({ onStepCompleted }) => {
  const [isLoading, setIsLoading] = useState(false);
  const [urlValue, setUrlValue] = useState('');
  const [bucketValue, setBucketValue] = useState('');
  const [keyValue, setKeyValue] = useState('');
  const [secretValue, setSecretValue] = useState('');
  const [regionValue, setRegionValue] = useState<Option | null>(null);

  useEnterKey(async () => {
    if (!isLoading) {
      setIsLoading(true);
      try {
        const data = {
          apiUrl: urlValue,
          bucket: bucketValue,
          key: keyValue,
          secret: secretValue,
          region: regionValue && regionValue.value,
        };
        await axios.put('/api/configs/s3', data);
        onStepCompleted();
      } catch (e) {
        console.error(e);
      }
    }
  });

  return (
    <>
      <h2>Configure S3</h2>
      <FieldsContainer>
        <InputContainer>
          <label htmlFor="url">API URL</label>
          <input id="url" value={urlValue} onChange={e => setUrlValue(e.target.value)} />
        </InputContainer>
        <InputContainer>
          <label htmlFor="bucket">Bucket</label>
          <input id="bucket" value={bucketValue} onChange={e => setBucketValue(e.target.value)} />
        </InputContainer>
        <InputContainer>
          <label htmlFor="key">Key</label>
          <input id="key" value={keyValue} onChange={e => setKeyValue(e.target.value)} />
        </InputContainer>
        <InputContainer>
          <label htmlFor="secret">Secret</label>
          <input id="secret" value={secretValue} onChange={e => setSecretValue(e.target.value)} />
        </InputContainer>
        <InputContainer>
          <label htmlFor="region">Region</label>
          <Select options={regionOptions} value={regionValue} onInput={setRegionValue} />
        </InputContainer>
      </FieldsContainer>
      {isLoading && (
        <LoadingContainer>
          <StyledSpan>Configuring your storage...</StyledSpan>
          <CircularProgress />
        </LoadingContainer>
      )}
    </>
  );
};

const FieldsContainer = styled.div`
  display: flex;
  flex-direction: column;
  margin-top: 30px;

  input {
    & + input {
      margin-top: 15px;
    }
  }
`;

const InputContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;

  label {
    margin-right: 20px;
    font-weight: 300;
    width: 60px;
  }

  input {
    padding: 10px 15px;
    border-radius: 4px;
    font-size: 1.1rem;
    font-family: 'Muli';
    border: 1px solid var(--gray);
    width: 300px;
  }

  :not(:last-child) {
    margin-bottom: 20px;
  }
`;

const StyledSpan = styled.span`
  margin-right: 15px;
`;

const LoadingContainer = styled.div`
  margin-top: 15px;

  display: flex;
  align-items: center;
`;

export default S3Details;
