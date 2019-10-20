import React, { useState, useEffect } from 'react';
import styled from 'styled-components';
import axios from 'axios';
import { FileInfo } from '../../types/fileInfo';
import Table from './table';
import { Pageable } from '../../types/pageable';
import { FilesWs } from '../../ws/files-ws';

const MainView: React.FC = () => {
  const [files, setFiles] = useState<FileInfo[]>([]);
  const [currentPage, setCurrentPage] = useState(0);
  const [pageSize, setPageSize] = useState(20);
  const [totalPages, setTotalPages] = useState(0);
  const [totalCount, setTotalCount] = useState(0);

  const fetchFiles = useCallback(async () => {
    try {
      const response = await axios.get(`/api/files?page=${currentPage}&size=${pageSize}`);
      const fetchedFiles = response.data as Pageable<FileInfo>;
      setFiles(fetchedFiles.content);
      setCurrentPage(fetchedFiles.number);
      setTotalPages(fetchedFiles.totalPages);
    } catch (error) {
      console.error(error);
    }
  }, [currentPage, pageSize]);

  const onWsEvent = async () => {
    if (currentPage === 0) {
      await fetchFiles();
    }
  };

  useEffect(() => {
    fetchFiles();
  }, [fetchFiles]);

  const sortedFiles = files.sort((f1, f2) => f2.uploadDateTime.localeCompare(f1.uploadDateTime));

  const refreshFile = (filename: string) => {
    const fileIndex = files.findIndex(f => f.filename === filename);
    if (fileIndex === -1) {
      // file no longer on displayed page
      return;
    }
    const file = files[fileIndex];
    const updatedFile: FileInfo = {
      ...file,
      isRefreshing: true,
    };
    const updatedFiles = [...files.slice(0, fileIndex), updatedFile, ...files.slice(fileIndex + 1, files.length)];
    setFiles(updatedFiles);
  };

  return (
    <StyledContainer>
      <FilesWs onFile={() => onWsEvent()}></FilesWs>
      <MainHeader>File list</MainHeader>
      <Table
        sortedFiles={sortedFiles}
        refreshFile={refreshFile}
        currentPage={currentPage}
        pageSize={pageSize}
        totalFiles={totalCount}
        onChangePage={(a: any) => {
          console.log(a);
        }}
        onChangePageSize={fetchFiles}
      />
    </StyledContainer>
  );
};

const MainHeader = styled.h1`
  font-size: 4em;
`;

const StyledContainer = styled.div`
  width: 80%;
  margin: 0 auto;
`;

export default MainView;
