package com.dnevi.healthcare.domain.repository;

import com.dnevi.healthcare.domain.model.invitation.InvitationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Optional;

public interface InvitationTokenRepository extends JpaRepository<InvitationToken, Long> {

    @Query("SELECT it FROM InvitationToken it "
            + "WHERE it.invitation.receiver = :receiver")
    Optional<InvitationToken> findOneBy(@Param("receiver") String receiver);

    @Query(value = "SELECT it FROM InvitationToken it "
            + "WHERE FUNCTION('sha1', "
            + "CONCAT("
            + "it.invitation.receiver, "
            + "'_', "
            + "it.hash)"
            + ") = :encodedToken")
    Optional<InvitationToken> findOneByEncodedToken(@Param("encodedToken") String encodedToken);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,
            value = ""
                    + "DELETE invitation_token "
                    + "FROM invitation_token "
                    + "INNER JOIN invitation "
                    + "ON invitation_token.invitation_id = invitation.id "
                    + "WHERE invitation.status = 'CONFIRMED' "
                    + "OR invitation.expiration < NOW()")
    void deleteAllExpiredOrConfirmed();
}
