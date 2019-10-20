import React from 'react';
import ReactSelect from 'react-select';
import styled from 'styled-components';

export interface Option {
  label: string;
  value: string;
}

interface SelectProps {
  options: Option[];
  value?: Option | null;
  onInput?: (value: Option) => void;
}

const Select: React.FC<SelectProps> = ({ options, value, onInput, ...otherProps }) => {
  const onChange = (option: Option) => {
    console.log(option.value);
    onInput && onInput(option);
  };
  return (
    <StyledSelect
      options={options}
      {...otherProps}
      styles={{
        control: (base: any) => ({
          ...base,
          border: '1px solid var(--gray)',
          fontSize: '1.1rem',
          height: '44px',
          paddingLeft: '2px',
        }),
      }}
      value={value}
      onChange={onChange}
    />
  );
};

const StyledSelect = styled(ReactSelect)`
  width: 300px;
`;

export default Select;
