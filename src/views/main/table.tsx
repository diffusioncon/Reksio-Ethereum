import React from 'react';
import styled from 'styled-components';
import {
  Table as MuiTable,
  TableHead,
  TableRow,
  TableCell,
  TableBody,
  Tooltip,
  IconButton,
  CircularProgress,
} from '@material-ui/core';
import CheckIcon from '@material-ui/icons/Check';
import ErrorIcon from '@material-ui/icons/Error';
import RefreshIcon from '@material-ui/icons/Refresh';
import { FileInfo } from '../../types/fileInfo';
import { isStatusValid, isoToDateTime, isoToDate } from '../../utils';

interface TableProps {
  sortedFiles: FileInfo[];
  refreshFile: (file: string) => void;
}

const Table: React.FC<TableProps> = ({ sortedFiles, refreshFile }) => (
  <MuiTable stickyHeader>
    <TableHead>
      <TableRow>
        <TableCell>File name</TableCell>
        <HashTableCell align="center">Hash</HashTableCell>
        <TableCell align="center">Uploaded on</TableCell>
        <TableCell align="center">Last status</TableCell>
        <TableCell />
      </TableRow>
    </TableHead>
    <TableBody>
      {sortedFiles.map(file => {
        const isValid = isStatusValid(file.status);
        return (
          <TableRow key={file.filename}>
            <TableCell component="th" scope="row">
              {file.filename}
            </TableCell>
            <HashTableCell align="center">
              <Tooltip title={file.hash} interactive>
                <OverflowContainer>
                  <span>{file.hash}</span>
                </OverflowContainer>
              </Tooltip>
            </HashTableCell>
            <TableCell align="center">
              <Tooltip title={isoToDateTime(file.uploadDateTime)} interactive>
                <div>{isoToDate(file.uploadDateTime)}</div>
              </Tooltip>
            </TableCell>
            <StatusTableCell align="center" isValid={isValid}>
              <Tooltip title={isValid ? "All's good" : "Something's wrong"}>
                <IconContainer>{isValid ? <CheckIcon /> : <ErrorIcon />}</IconContainer>
              </Tooltip>
            </StatusTableCell>
            <TableCell>
              <IconContainer>
                {!file.isRefreshing ? (
                  <IconButton aria-label="refresh" size="medium" onClick={() => refreshFile(file.filename)}>
                    <RefreshIcon />
                  </IconButton>
                ) : (
                  <CircularProgress size={48} />
                )}
              </IconContainer>
            </TableCell>
          </TableRow>
        );
      })}
    </TableBody>
  </MuiTable>
);

interface StatusTableCellProps {
  readonly isValid: boolean;
}

const StatusTableCell = styled(({ isValid, ...rest }) => <TableCell {...rest} />)<StatusTableCellProps>`
  color: ${props => (props.isValid ? 'green' : 'red')};
`;

const IconContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
`;

const HashTableCell = styled(TableCell)`
  width: 200px;
  max-width: 200px;
`;

const OverflowContainer = styled.div`
  text-overflow: ellipsis;
  overflow: hidden;
`;

export default Table;
