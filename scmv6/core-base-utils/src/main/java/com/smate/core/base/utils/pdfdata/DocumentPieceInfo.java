package com.smate.core.base.utils.pdfdata;

import com.itextpdf.text.pdf.PdfDate;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;

/**
 * 往pdf中加入隐藏私有数据
 * 
 * @author HWQ
 *
 */
public class DocumentPieceInfo {
  static PdfName PIECE_INFO = new PdfName("PieceInfo");
  static PdfName LAST_MODIFIED = new PdfName("LastModified");
  static PdfName PRIVATE = new PdfName("Private");

  public void addPieceInfo(PdfReader reader, PdfName app, PdfName name, PdfObject value) {
    PdfDictionary catalog = reader.getCatalog();
    PdfDictionary pieceInfo = catalog.getAsDict(PIECE_INFO);
    if (pieceInfo == null) {
      pieceInfo = new PdfDictionary();
      catalog.put(PIECE_INFO, pieceInfo);
    }

    PdfDictionary appData = pieceInfo.getAsDict(app);
    if (appData == null) {
      appData = new PdfDictionary();
      pieceInfo.put(app, appData);
    }

    PdfDictionary privateData = appData.getAsDict(PRIVATE);
    if (privateData == null) {
      privateData = new PdfDictionary();
      appData.put(PRIVATE, privateData);
    }

    appData.put(LAST_MODIFIED, new PdfDate());
    privateData.put(name, value);
  }

  public PdfObject getPieceInfo(PdfReader reader, PdfName app, PdfName name) {
    PdfDictionary catalog = reader.getCatalog();

    PdfDictionary pieceInfo = catalog.getAsDict(PIECE_INFO);
    if (pieceInfo == null)
      return null;

    PdfDictionary appData = pieceInfo.getAsDict(app);
    if (appData == null)
      return null;

    PdfDictionary privateData = appData.getAsDict(PRIVATE);
    if (privateData == null)
      return null;

    return privateData.get(name);
  }
}
