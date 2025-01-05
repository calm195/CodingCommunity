package cor.chrissy.community.service.user.repository.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wx128
 * @createAt 2025/1/4
 */
@Data
public class IpInfo implements Serializable {
    private static final long serialVersionUID = -4612222921661930429L;

    private String firstIp;

    private String firstRegion;

    private String latestIp;

    private String latestRegion;
}
