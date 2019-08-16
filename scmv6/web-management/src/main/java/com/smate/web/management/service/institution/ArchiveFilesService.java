package com.smate.web.management.service.institution;

import com.smate.core.base.file.model.ArchiveFile;
import com.smate.web.management.model.institution.bpo.FileUploadSimple;

public interface ArchiveFilesService {


  FileUploadSimple uploadInsFax(FileUploadSimple fileUploadSimple) throws Exception;

  FileUploadSimple uploadInsLogo(FileUploadSimple fileUploadSimple) throws Exception;

  FileUploadSimple saveArchiveFundFiles(FileUploadSimple fileUploadSimple) throws Exception;

  ArchiveFile getArchiveFiles(Long currentFileId) throws Exception;


}
