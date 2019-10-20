import React from 'react';
import styled from 'styled-components';
import { CSSTransition, SwitchTransition } from 'react-transition-group';
import './index.scss';
import WelcomeStep from './steps/Welcome';
import StorageStep from './steps/Storage';
import DLTStep from './steps/DLT';
import { Redirect } from 'react-router';

const steps = [WelcomeStep, StorageStep, DLTStep];

class ConfigView extends React.Component {
  state = {
    stepIdx: 0,
    redirectToList: false,
  };

  componentDidMount() {
    window.addEventListener('keydown', this.onKeyDown);
  }

  componentWillUnmount() {
    window.removeEventListener('keydown', this.onKeyDown);
  }

  onKeyDown = (e: KeyboardEvent) => {
    if (e.key === 'Enter') {
      this.onNextStep();
    }
  };

  onNextStep = () => {
    const { stepIdx } = this.state;

    const newStepIdx = stepIdx + 1;

    if (newStepIdx >= steps.length) {
      this.setState({
        redirectToList: true,
      });
      return;
    }

    this.setState({
      stepIdx: stepIdx + 1,
    });
  };

  render() {
    const { stepIdx, redirectToList } = this.state;

    if (redirectToList) {
      return <Redirect to="/main" />;
    }

    const Component = steps[stepIdx];

    return (
      <StepContainer>
        <SwitchTransition>
          <CSSTransition key={stepIdx} timeout={700} classNames="slide" unmountOnExit>
            <StepContainer>
              <Component />
            </StepContainer>
          </CSSTransition>
        </SwitchTransition>
      </StepContainer>
    );
  }
}

const StepContainer = styled.div`
  width: 100vw;
  height: 100vh;

  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
`;

export default ConfigView;
