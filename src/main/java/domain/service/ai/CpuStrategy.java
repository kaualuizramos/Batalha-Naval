cat << 'EOF' > src/main/java/domain/service/ai/CpuStrategy.java
package domain.service.ai;

import domain.model.Coordinate;
import java.util.Random;

public interface CpuStrategy {
    Coordinate chooseTarget(char[][] trackingGrid, Random rng);
}
EOF