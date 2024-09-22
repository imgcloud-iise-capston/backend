package iise_capston.imgcloud.domain.repository;

import iise_capston.imgcloud.member.OrderMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderMemberRepository extends JpaRepository<OrderMember,Long> {
}
