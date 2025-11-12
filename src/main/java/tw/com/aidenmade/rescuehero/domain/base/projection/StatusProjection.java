package tw.com.aidenmade.rescuehero.domain.base.projection;

public interface StatusProjection {
    Long getId();
    AuditInfoProjection getAuditInfo();
//    TODO: DB 沒有這些欄位，但未來可能增加
//    DisasterProjection getDisaster();
//    StatusTypeProjection getType();
//    String getCode();
    String getName();
    String getDescription();
}
