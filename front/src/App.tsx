import React from 'react';
import { BrowserRouter as Router, Switch, Route, Redirect } from 'react-router-dom';
import { StylesProvider, createMuiTheme, ThemeProvider } from '@material-ui/core/styles';
import './style.scss';
import { CssBaseline } from '@material-ui/core';
import ConfigView from './views/config';
import MainView from './views/main';

const theme = createMuiTheme({
  typography: {
    fontFamily: [
      'Muli',
      '"Helvetica Neue"',
      'Arial',
      'sans-serif',
      '"Apple Color Emoji"',
      '"Segoe UI Emoji"',
      '"Segoe UI Symbol"',
    ].join(','),
  },
});

const App: React.FC = () => (
  <StylesProvider injectFirst>
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <Router>
        <Switch>
          <Route path="/list">
            <MainView />
          </Route>
          <Route path="/config">
            <ConfigView />
          </Route>
          <Route>
            <Redirect to="/list" />
          </Route>
        </Switch>
      </Router>
    </ThemeProvider>
  </StylesProvider>
);

export default App;
