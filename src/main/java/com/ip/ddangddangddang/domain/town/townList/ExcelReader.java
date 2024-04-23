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
            // 엑셀 파일 열기
            Workbook workbook = WorkbookFactory.create(file);
            int sheets = workbook.getNumberOfSheets();

            // 총 시트 수만큼 for문 -> 모든 시트 돌려보기
            for (int i = 0; i < sheets; i++) {
                // i 번째 시트 가져오기
                Sheet sheet = workbook.getSheetAt(i);

                // 모든 행 반복
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
