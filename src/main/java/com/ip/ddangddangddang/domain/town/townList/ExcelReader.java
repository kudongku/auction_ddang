package com.ip.ddangddangddang.domain.town.townList;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class ExcelReader {

    private final TownListRepository townListRepository;

    public void createTownList() {
        List<TownList> townList = new ArrayList<>();

        try (FileInputStream file = new FileInputStream(
            ("src/main/resources/townlist.xlsx"))) {

            Workbook workbook = WorkbookFactory.create(file);
            int sheets = workbook.getNumberOfSheets();

            for (int i = 0; i < sheets; i++) {
                Sheet sheet = workbook.getSheetAt(i);

                for (int j = 1; j < sheet.getPhysicalNumberOfRows(); j++) {
                    Row row = sheet.getRow(j);

                    townList.add(
                        new TownList(
                            row.getCell(0).getStringCellValue(),
                            row.getCell(1).getStringCellValue(),
                            row.getCell(2).getStringCellValue(),
                            row.getCell(3).getStringCellValue(),
                            row.getCell(4).getStringCellValue(),

                            row.getCell(5).getNumericCellValue(),
                            row.getCell(6).getNumericCellValue())
                    );
                }
            }
        } catch (IOException | EncryptedDocumentException /*| InvalidFormatException*/ e) {
            e.printStackTrace();
        }
        townListRepository.saveAll(townList);
    }

}
