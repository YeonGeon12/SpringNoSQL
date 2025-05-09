package kopo.poly.service.impl;

import kopo.poly.dto.MelonDTO;
import kopo.poly.persistance.mongodb.IMelonMapper;
import kopo.poly.service.IMelonService;
import kopo.poly.util.CmmUtil;
import kopo.poly.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class MelonService implements IMelonService {

    private final IMelonMapper melonMapper;

    /**
     * 멜론 차트 수집 함수
     */
    private List<MelonDTO> doCollect() throws Exception {

        log.info("{}.doCollect Start!", this.getClass().getName());

        List<MelonDTO> pList = new LinkedList<>();

        // 멜론 Top100 중 50위까지 정보 가져오는 페이지
        String url = "https://www.melon.com/chart/index.htm";

        // JSOUP 라이브러리를 통해 사이트 접속되면, 그 사이트의 전체 HTML 소스 저장할 변수
        Document doc = Jsoup.connect(url).get();

        // <div class="service_list_song"> 이 태그 내에서 있는 HTML 소스만 element에 저장됨
        Elements element = doc.select("div.service_list_song");

        // Iterator을 사용하여 멜론차트 정보를 가져오기
        for (Element songInfo : element.select("div.wrap_song_info")) {

            // 크롤링을 통해 데이터 저장하기
            String song = CmmUtil.nvl(songInfo.select("div.ellipsis.rank01 a").text()); // 노래
            String singer = CmmUtil.nvl(songInfo.select("div.ellipsis.rank02 a").eq(0).text());

            log.info("song : {}", song);
            log.info("singer : {}", singer);

            // 가수와 노래 정보가 모두 수집되었다면, 저장함
            if ((!song.isEmpty()) && (!singer.isEmpty())) {

                MelonDTO pDTO = MelonDTO.builder().collectTime(DateUtil.getDateTime("yyyyMMddhhmmss"))
                        .song(song).singer(singer).build();

                // 한번에 여러개의 데이터를 MongoDB에 저장할 List 형태의 데이터 저장하기
                pList.add(pDTO);

            }
        }

        log.info("{}.doCollect End!", this.getClass().getName());

        return pList;

    }

    @Override
    public int collectMelonSong() throws Exception {

        int res;

        // 생성할 컬랙션 명
        String colNm = "MELON_" + DateUtil.getDateTime("yyyyMMdd");

        // private 함수로 선언된 doCollect 함수를 호출하여 결과를 받기
        List<MelonDTO> rList = this.doCollect();

        // MongoDB에 데이터 저장하기
        res = melonMapper.insertSong(rList, colNm);

        // 로그찍기
        log.info("{}.collectMelonSong End!", this.getClass().getName());

        return res;
    }

    @Override
    public List<MelonDTO> getSongList() throws Exception {

        log.info("{}.getSongList Start!", this.getClass().getName());

        // MongoDB에 저장된 컬랙션 이름
        String colNm = "MELON_" + DateUtil.getDateTime("yyyyMMdd");

        List<MelonDTO> rList = melonMapper.getSongList(colNm);

        log.info("{}.getSongList End!", this.getClass().getName());

        return rList;
    }

    @Override
    public List<MelonDTO> getSingerSongCnt() throws Exception {

        log.info("{}.getSingerSongCnt Start!", this.getClass().getName());

        String colNm = "MELON_" + DateUtil.getDateTime("yyyyMMdd");

        List<MelonDTO> rList = melonMapper.getSingerSongCnt(colNm);

        log.info("{}.getSingerSongCnt End!", this.getClass().getName());

        return rList;
    }

    // 가수 노래 수집하고 조회하기
    @Override
    public List<MelonDTO> getSingerSong(MelonDTO pDTO) throws Exception {

        log.info("{}.getSingerSong Start!", this.getClass().getName());

        // MongoDB에 저장된 컬랙션 이름
        String colNm = "MELON_" + DateUtil.getDateTime("yyyyMMdd");

        // 결과값
        List<MelonDTO> rList = null;

        // Melon 노래 수집하기
        if (this.collectMelonSong() == 1) {

            // 가수 노래 조회하기
            rList = melonMapper.getSingerSong(colNm, pDTO);

        }

        log.info("{}.getSingerSong End!", this.getClass().getName());

        return rList;

    }

    // 오늘 생성된 컬렉션 삭제
    @Override
    public int dropCollection() throws Exception {

        log.info("{}.dropCollection Start!", this.getClass().getName());

        int res;

        // MongoDB에 저장된 컬랙션 이름
        String colNm = "MELON_" + DateUtil.getDateTime("yyyyMMdd");

        // 기존 수집된 멜론 Top100 수집한 컬렉션 삭제하기
        res = melonMapper.dropCollection(colNm);

        log.info("{}.dropCollection End!", this.getClass().getName());

        return res;
    }

    // MongoDB에 저장하기 위한 Mapper 호출
    @Override
    public List<MelonDTO> insertManyField() throws Exception {

        // 로그찍기
        log.info("{}.insertManyField Start!", this.getClass().getName());

        List<MelonDTO> rList = null;

        // 생성할 컬랙션 명
        String colNm = "MELON_" + DateUtil.getDateTime("yyyyMMdd");

        // MongoDB에 데이터 저장하기
        if (melonMapper.insertManyField(colNm, this.doCollect()) == 1) {

            // 변경된 값을 확인하기 위해 MongoDB로부터 데이터 조회하기
            rList = melonMapper.getSongList(colNm);

        }

        // 로그 찍기
        log.info("{}.insertManyField End!", this.getClass().getName());

        return rList;
    }

    // 가수 이름 변경
    @Override
    public List<MelonDTO> updateField(MelonDTO pDTO) throws Exception {

        // 로그 찍기
        log.info("{}.updateField Start!", this.getClass().getName());

        List<MelonDTO> rList = null; // 변경된 데이터 조회 결과

        // 수정할 컬렉션
        String colNm = "MELON_" + DateUtil.getDateTime("yyyyMMdd");

        // 기존 수집된 멜론Top100 수집한 컬렉션 삭제하기
        melonMapper.dropCollection(colNm);

        // 멜론Top100 수집하기
        if (this.collectMelonSong() == 1) {

            // 예 : singer 필드에 저장된 '방탄소냔딘' 값을 'BTS'로 변경하기
            if (melonMapper.updateField(colNm, pDTO) == 1) {

                // 변경된 값을 확인하기 위해 MongoDB로부터 데이터 조회하기
                rList = melonMapper.getUpdateSinger(colNm, pDTO);

            }
        }

        // 로그찍기
        log.info("{}.updateField End!", this.getClass().getName());

        return rList;
    }

    @Override
    public List<MelonDTO> updateAddField(MelonDTO pDTO) throws Exception {

        // 로그 찍기
        log.info("{}.updateAddField Start!", this.getClass().getName());

        List<MelonDTO> rList = null;

        // 수정할 컬랙션
        String colNm = "MELON_" + DateUtil.getDateTime("yyyyMMdd");

        // 기존 수집괸 멜론Top100 수집한 컬렉션 삭제하기
        melonMapper.dropCollection(colNm);

        // 멜론Top100 수집하기
        if (this.collectMelonSong() == 1) {

            // 예 : nickname 필드를 추가하고, nickname 필드 값은 'BTS' 저장하기
            if (melonMapper.updateAddField(colNm, pDTO) == 1) {

                // 변경된 값을 확인하기 위해 MongoDB로부터 데이터 조회하기
                rList = melonMapper.getSingerSongNickname(colNm, pDTO);

            }
        }

        // 로그 찍기
        log.info("{}.updateAddField End!", this.getClass().getName());

        return rList;
    }
}
