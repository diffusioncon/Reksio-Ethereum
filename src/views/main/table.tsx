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
  TablePagination,
  LinearProgress,
} from '@material-ui/core';
import CheckIcon from '@material-ui/icons/Check';
import ErrorIcon from '@material-ui/icons/Error';
import RefreshIcon from '@material-ui/icons/Refresh';
import { FileInfo, Status } from '../../types/fileInfo';
import { isoToDateTime, isoToDate, getFileStatus } from '../../utils';

interface TableProps {
  sortedFiles: FileInfo[];
  currentPage: number;
  pageSize: number;
  totalFiles: number;
  isLoading: boolean;
  refreshFile: (file: FileInfo) => void;
  onChangePage: (_: any, page: number) => void;
  onChangePageSize: (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => void;
}

const Table: React.FC<TableProps> = ({
  sortedFiles,
  currentPage,
  pageSize,
  totalFiles,
  refreshFile,
  onChangePage,
  onChangePageSize,
  isLoading,
}) => (
  <>
    <MuiTable stickyHeader>
      <TableHead>
        <TableRow>
          <TableCell>File name</TableCell>
          <HashTableCell align="center">Hash</HashTableCell>
          <TableCell align="center">Uploaded on</TableCell>
          <TableCell align="center">Last status</TableCell>
          <TableCell align="center">Last checked</TableCell>
          <TableCell />
        </TableRow>
      </TableHead>
      {!isLoading && (
        <TableBody>
          {sortedFiles.map(file => {
            const status = getFileStatus(file);
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
                <StatusTableCell align="center" isValid={status === Status.VALID}>
                  <Tooltip
                    title={
                      status === Status.VALID
                        ? 'Hash is matching'
                        : status === Status.INVALID
                        ? 'Hash mismatch'
                        : 'DLT notarization pending'
                    }
                  >
                    <IconContainer>
                      {status === Status.VALID ? (
                        <CheckIcon />
                      ) : status === Status.INVALID ? (
                        <ErrorIcon />
                      ) : (
                        <CircularProgress />
                      )}
                    </IconContainer>
                  </Tooltip>
                </StatusTableCell>
                <TableCell align="center">
                  <Tooltip title={isoToDateTime(file.hashCalculationDateTime)} interactive>
                    <div>{isoToDate(file.hashCalculationDateTime)}</div>
                  </Tooltip>
                </TableCell>
                <TableCell>
                  <IconContainer>
                    {!file.isRefreshing ? (
                      <IconButton aria-label="refresh" size="medium" onClick={() => refreshFile(file)}>
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
      )}
    </MuiTable>
    {isLoading && <LinearProgress />}
    <TablePagination
      component="div"
      count={totalFiles}
      rowsPerPage={pageSize}
      page={currentPage}
      onChangePage={onChangePage}
      onChangeRowsPerPage={onChangePageSize}
    />
  </>
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
