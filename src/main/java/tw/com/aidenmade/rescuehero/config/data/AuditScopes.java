package tw.com.aidenmade.rescuehero.config.data;


import java.util.function.Supplier;

public final class AuditScopes {
    private AuditScopes() {}

    public static final ScopedValue<Boolean> AUDIT_BYPASS = ScopedValue.newInstance();
    public static final ScopedValue<Long> AUDITOR_ID_OVERRIDE = ScopedValue.newInstance();

    // 在這個區塊內「暫停審計」
    public static <R> R runWithoutAuditing(Supplier<R> body) {
        return ScopedValue.getWhere(AUDIT_BYPASS, Boolean.TRUE, body);
    }

    // 在這個區塊內「指定審計帳號」
    public static <R> R runAsAuditor(long accountId, Supplier<R> body) {
        return ScopedValue.getWhere(AUDITOR_ID_OVERRIDE, accountId, body);
    }
}