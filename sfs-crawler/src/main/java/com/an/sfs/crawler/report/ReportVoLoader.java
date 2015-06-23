package com.an.sfs.crawler.report;

import java.util.List;
import java.util.Map;

import com.an.sfs.crawler.FileUtil;
import com.an.sfs.crawler.ccjg.CcjgLoader;
import com.an.sfs.crawler.cwfx.CwfxLoader;
import com.an.sfs.crawler.cwfx.CwfxProfitUpLoader;
import com.an.sfs.crawler.cwfx.CwfxVo;
import com.an.sfs.crawler.gdyj.GdrsDownLoader;
import com.an.sfs.crawler.name.IgnoreStockLoader;
import com.an.sfs.crawler.name.IndustryLoader;
import com.an.sfs.crawler.name.WhiteHorseStockLoader;
import com.an.sfs.crawler.tdx.StockLoader;
import com.an.sfs.crawler.tdx.StockVo;

public class ReportVoLoader {
    /**
     * @param codeList
     * @param reportVoMap
     */
    public static void loadReportVo(List<String> codeList, Map<String, String> ronaMap, Map<String, String> rotaMap,
            Map<String, String> dtarMap, List<ReportVo> reportVoList) {
        int i = 1;
        for (String code : codeList) {
            ReportVo vo = new ReportVo();
            vo.setCode(code);
            vo.setIndex(String.format("%04d", i++));

            String name = StockLoader.getInst().getStockName(code);
            if (name == null) {
                name = IndustryLoader.getInst().getIndustryName(code);
            }
            if (name == null) {
                System.out.println(code);
            }
            if (name.length() < 4) {
                String prefix = "";
                for (int j = 0; j < 4 - name.length(); j++) {
                    prefix += "&nbsp&nbsp&nbsp&nbsp";
                }
                name = prefix + name;
            }
            vo.setName(name);

            long jgcc = CcjgLoader.getInst().getTotal(code);
            vo.setJgcc(FileUtil.FLOAT_FORMAT.format((float) jgcc / 10000f) + "万");
            CwfxVo cwfxVo = CwfxLoader.getInst().getCwfxVo(code);
            StockVo tStockVo = StockLoader.getInst().getTStockVo(code);
            if (cwfxVo != null && tStockVo != null) {
                float pe = tStockVo.getPrice() / cwfxVo.getPeps();
                float pb = tStockVo.getPrice() / cwfxVo.getNaps();
                vo.setPe(FileUtil.FLOAT_FORMAT.format(pe));
                vo.setPb(FileUtil.FLOAT_FORMAT.format(pb));
            }

            String rona = ronaMap.get(code);
            String rota = rotaMap.get(code);
            String dtar = dtarMap.get(code);
            vo.setRona(rona);
            vo.setRota(rota);
            vo.setDtar(dtar);

            String note = "";
            if (IgnoreStockLoader.getInst().isIgnore(code)) {
                note += " | IGNORE";
            } else if (WhiteHorseStockLoader.getInst().isWhiteHorse(code)) {
                note += " | WHITEHORSE";
            }

            if (GdrsDownLoader.getInst().isGdrsDown(code)) {
                note += " | GDRS";
            }
            if (CwfxProfitUpLoader.getInst().isProfitUp(code)) {
                note += " | PROFIT";
            }
            vo.setNote(note);

            reportVoList.add(vo);
        }
    }
}
