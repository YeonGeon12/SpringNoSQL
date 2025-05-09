package kopo.poly.controller;

import kopo.poly.controller.response.CommonResponse;
import kopo.poly.dto.MelonDTO;
import kopo.poly.dto.MsgDTO;
import kopo.poly.service.impl.MelonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequestMapping(value = "/melon/v1")
@RequiredArgsConstructor
@RestController
public class MelonController {

    private final MelonService melonService;

    /**
     * 멜론 노래 리스트 저장하기
     */
    @PostMapping(value = "collectMelonSong")
    public ResponseEntity<CommonResponse<MsgDTO>> collectMelonSong() throws Exception {

        log.info("{}.collectMelonSong Start!", this.getClass().getName());

        // 수집 결과 출력
        String msg;

        int res = melonService.collectMelonSong();

        if (res == 1) {
            msg = "멜론차트 수집 성공!";

        } else {
            msg = "멜론차트 수집 실패!";
        }

        MsgDTO dto = MsgDTO.builder().result(res).msg(msg).build();

        log.info("{}.collectMelonSong End!", this.getClass().getName());

        return ResponseEntity.ok(
                CommonResponse.of(HttpStatus.OK, HttpStatus.OK.series().name(), dto));
    }

    /**
     * 오늘 수집된 멜론 노래 리스트 가져오기
     */
    @PostMapping(value = "getSongList")
    public ResponseEntity<CommonResponse<List<MelonDTO>>> getSongList() throws Exception {

        log.info("{}.getSongList Start!", this.getClass().getName());

        // JAVA8 부타 제공되는 Optional 활용하여 NPE(Null Painter Exception) 처리
        List<MelonDTO> rList = Optional.ofNullable(melonService.getSongList())
                .orElseGet(ArrayList::new);

        log.info("{}.getSongList End!", this.getClass().getName());

        return ResponseEntity.ok(
                CommonResponse.of(HttpStatus.OK, HttpStatus.OK.series().name(), rList));
    }

    /**
     * 가수별 수집된 노래의 수 가져오기
     */
    @PostMapping(value = "getSingerSongCnt")
    public ResponseEntity<CommonResponse<List<MelonDTO>>> getSingerSongCnt()
            throws Exception {

        log.info("{}.getSingerSongCnt Start!", this.getClass().getName());

        // Java8 부터 제공되는 Optional 활용하여 NPE 처리
        List<MelonDTO> rList = Optional.ofNullable(melonService.getSingerSongCnt())
                .orElseGet(ArrayList::new);

        log.info("{}.getSingerSongCnt End!", this.getClass().getName());

        return ResponseEntity.ok(
                CommonResponse.of(HttpStatus.OK, HttpStatus.OK.series().name(), rList));
    }

    /**
     * 가수 이름으로 조회하기
     */
    @PostMapping(value = "getSingerSong")
    public ResponseEntity<CommonResponse<List<MelonDTO>>> getSingerSong(@RequestBody MelonDTO pDTO)
            throws Exception {

        log.info("{}.getSingerSong Start!", this.getClass().getName());

        log.info("pDTO: {}", pDTO); // JSON 구조로 받은 값이 잘 받았는지 확인라기 위해 로그 찍기

        // Java8부터 제공되는 Optional 활용하여 NPE 처리
        List<MelonDTO> rList = Optional.ofNullable(melonService.getSingerSong(pDTO))
                .orElseGet(ArrayList::new);

        log.info("{}.getSingerSong End!", this.getClass().getName());

        return ResponseEntity.ok(
                CommonResponse.of(HttpStatus.OK, HttpStatus.OK.series().name(), rList));
    }

    /**
     * 수집된 멜론 차트 컬렉션 삭제하기
     */
    @PostMapping(value = "dropCollection")
    public ResponseEntity<CommonResponse<MsgDTO>> dropCollection() throws Exception {

        log.info("{}.dropCollection Start!", this.getClass().getName());

        // 삭제 결과 출력
        String msg;

        int res = melonService.dropCollection();

        if (res == 1) {
            msg = "멜론차트 삭제 성공!";

        } else {
            msg = "멜론차트 삭제 실패!";
        }

        MsgDTO dto = MsgDTO.builder().result(res).msg(msg).build();

        log.info("{}.dropCollection End!", this.getClass().getName());

        return ResponseEntity.ok(
                CommonResponse.of(HttpStatus.OK, HttpStatus.OK.series().name(), dto));
    }

    /**
     * 멜론 노래 리스트 저장하기
     */
    @PostMapping(value = "insertManyField")
    public ResponseEntity<CommonResponse<List<MelonDTO>>> insertManyField() throws Exception {

        log.info("{}.insertManyField Start!", this.getClass().getName());

        // Java 8부터 제공되는 Optional 활용하여 NPE(Null Pointer Exception) 처리
        List<MelonDTO> rList = Optional.ofNullable(melonService.insertManyField())
                .orElseGet(ArrayList::new);

        log.info("{}.insertManyField End!", this.getClass().getName());

        return ResponseEntity.ok(
                CommonResponse.of(HttpStatus.OK, HttpStatus.OK.series().name(), rList));
    }

    // Ajax로 Controller 호출해 JSON으로 결과 받기
    /**
     * 가수 이름이 수정하기
     * 예 : 방탄소년단을 BTS로 변경하기
     */
    @PostMapping(value = "updateField")
    public ResponseEntity<CommonResponse<List<MelonDTO>>> updateField(@RequestBody MelonDTO pDTO) throws Exception {

        log.info("{}.updateField Start!", this.getClass().getName());

        log.info("pDTO :{}", pDTO); // JSON 구조로 받은 값이 잘 받았는지 확인하기 위해 로그 찍기

        // JAVA8 부터 제공되는 Optional 활용하여 NPE 처리
        List<MelonDTO> rList = Optional.ofNullable(melonService.updateField(pDTO))
                .orElseGet(ArrayList::new);

        log.info("{}.updateField End!", this.getClass().getName());

        return ResponseEntity.ok(
                CommonResponse.of(HttpStatus.OK, HttpStatus.OK.series().name(), rList));
    }

    /**
     * 가수 별명 추가하기
     * 예 : 방탄소년단을 BTS 별명 추가하기
     */
    @PostMapping(value = "updateAddField")
    public ResponseEntity<CommonResponse<List<MelonDTO>>> updateAddField(@RequestBody MelonDTO pDTO) throws Exception {

        log.info("{}.updateAddField Start!", this.getClass().getName());

        log.info("pDTO :{}", pDTO); // JSON 구조로 받은 값이 잘 받았는지 확인하기 위해 로그 찍기

        //JAVA 8부터 제공되는 Optional 활용하여 NPE 처리
        List<MelonDTO> rList = Optional.ofNullable(melonService.updateAddField(pDTO))
                .orElseGet(ArrayList::new);

        log.info("{}.updateAddField End!", this.getClass().getName());

        return ResponseEntity.ok(
                CommonResponse.of(HttpStatus.OK, HttpStatus.OK.series().name(), rList));
    }
}
