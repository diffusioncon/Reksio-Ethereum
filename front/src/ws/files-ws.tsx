import React from 'react';
import SockJsClient from 'react-stomp';
import { FileInfo } from '../types/fileInfo';
 
interface FilesWsProps {
  onFile: (file: FileInfo) => void;
}

export const FilesWs: React.FC<FilesWsProps> = ({onFile}) => {
  
  return (
    <>
      <SockJsClient url='/websocket' topics={['/files']}
          onMessage={(msg: FileInfo) => onFile(msg)} />
    </>
  );
}