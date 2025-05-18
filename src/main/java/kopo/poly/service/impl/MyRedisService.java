package kopo.poly.service.impl;

import kopo.poly.dto.RedisDTO;
import kopo.poly.persistance.redis.IMyRedisMapper;
import kopo.poly.service.IMyRedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class MyRedisService implements IMyRedisService {

    private final IMyRedisMapper myRedisMapper;

    @Override
    public RedisDTO saveString(RedisDTO pDTO) throws Exception {

        log.info("{}.saveString Start!", this.getClass().getName());

        // 저장할 RedisDB 키
        RedisDTO rDTO;

        int res = myRedisMapper.saveString(redisKey, pDTO);

        if (res == 1) {
            rDTO = myRedisMapper.getString(redisKey);

        } else {
            log.info("Redis 저장 실패!!");
            throw new Exception("Redis 저장 실패!!");

        }

        log.info("{}.saveString End!", this.getClass().getName());

        return rDTO;
    }
}
