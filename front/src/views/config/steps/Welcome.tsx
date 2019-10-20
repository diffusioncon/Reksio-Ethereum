import React from 'react';
import { useEnterKey } from '../../../utils';
import { StepProps } from '..';

const WelcomeStep: React.FC<StepProps> = ({ onStepCompleted }) => {
  useEnterKey(onStepCompleted);
  return <h2>It's time to configure Reksio</h2>;
};

export default WelcomeStep;
