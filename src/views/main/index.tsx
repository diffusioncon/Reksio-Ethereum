import React from 'react';
import styled from 'styled-components';
import axios from 'axios';
import { FileInfo } from '../../types/fileInfo';
import Table from './table';
import { Pageable } from '../../types/pageable';
import { FilesWs } from '../../ws/files-ws';

interface MainViewState {
  files: FileInfo[];
  currentPage: number;
  pageSize: number;
  totalPages: number;
  totalCount: number;
  isLoading: boolean;
}

export class MainView extends React.Component {
  state: MainViewState = {
    files: [],
    pageSize: 10,
    currentPage: 0,
    totalPages: 0,
    totalCount: 0,
    isLoading: true,
  };

  componentDidMount() {
    this.fetchFiles();
  }

  fetchFiles = async (ignoreLoader = false) => {
    if (!ignoreLoader) {
      this.setState({ isLoading: true });
    }
    const { currentPage, pageSize } = this.state;

    try {
      const response = await axios.get(
        `/api/files?page=${currentPage}&size=${pageSize}&sort=uploadDateTime&uploadDateTime.dir=desc`
      );
      const fetchedFiles = response.data as Pageable<FileInfo>;
      const newState = {
        files: fetchedFiles.content,
        currentPage: fetchedFiles.number,
        totalPages: fetchedFiles.totalPages,
        totalCount: fetchedFiles.totalElements,
        isLoading: false,
      };
      this.setState(newState);
    } catch (error) {
      console.error(error);
    }
  };

  onWsEvent = () => {
    const { currentPage } = this.state;
    if (currentPage === 0) {
      this.fetchFiles(true);
    }
  };

  updateFile(file: FileInfo) {
    const { files } = this.state;

    const fileIndex = files.findIndex(f => f.filename === file.filename);
    if (fileIndex === -1) {
      return;
    }
    const updatedFiles = [...files.slice(0, fileIndex), file, ...files.slice(fileIndex + 1, files.length)];
    this.setState({ files: updatedFiles });
  }

  refreshFile = async (file: FileInfo) => {
    const refreshingFile: FileInfo = {
      ...file,
      isRefreshing: true,
    };
    this.updateFile(refreshingFile);

    try {
      const response = await axios.post(`/api/files/${file.filename}`);
      const updatedFile = response.data as FileInfo;
      this.updateFile(updatedFile);
    } catch (e) {
      console.error(e);
    }
  };

  onChangePage = (_: any, page: number) => {
    this.setState(
      {
        currentPage: page,
      },
      this.fetchFiles
    );
  };

  onChangePageSize = (event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    this.setState(
      {
        pageSize: event.target.value,
      },
      this.fetchFiles
    );
  };

  render() {
    const { isLoading, files, currentPage, pageSize, totalCount } = this.state;

    return (
      <StyledContainer>
        <FilesWs onFile={this.onWsEvent} />
        <MainHeader>Notarized file list</MainHeader>
        <Table
          isLoading={isLoading}
          sortedFiles={files}
          refreshFile={this.refreshFile}
          currentPage={currentPage}
          pageSize={pageSize}
          totalFiles={totalCount}
          onChangePage={this.onChangePage}
          onChangePageSize={this.onChangePageSize}
        />
      </StyledContainer>
    );
  }
}

const MainHeader = styled.h1`
  font-size: 4em;
`;

const StyledContainer = styled.div`
  width: 80%;
  margin: 0 auto;
`;

export default MainView;
