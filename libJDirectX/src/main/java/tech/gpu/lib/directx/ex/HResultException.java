package tech.gpu.lib.directx.ex;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class HResultException extends RuntimeException {
    private final int hresult;
    private HResultSeverity severity;
    private HResultFacility facility;
    private HResultCode code;

    public HResultException(final String message, final int hresult) {
        super(message);
        this.hresult = hresult;
        int severity = (hresult >>> 31) & 0x1;
        this.severity = HResultSeverity.valueOf(severity);
        int facility = (hresult >>> 16) & 0x7FFF;
        this.facility = HResultFacility.valueOf(facility);
        int code = hresult & 0xFFFF;
        this.code = HResultCode.valueOf(code);
    }

    public HResultException(final int hresult) {
        this(null, hresult);
    }

    public enum HResultSeverity {
        SUCCESS(0), FAILURE(1);

        final int severityValue;
        HResultSeverity(int severityValue) {
            this.severityValue = severityValue;
        }

        static HResultSeverity valueOf(final int severityValue) {
            for (HResultSeverity severity : HResultSeverity.values()) {
                if (severity.severityValue == severityValue) { return severity; }
            }
            return FAILURE;
        }
    }

    public enum HResultFacility {
        /**
         * No specific facility.
         */
        FACILITY_NULL(0),
        /**
         * Remote Procedure Call.
         */
        FACILITY_RPC(1),
        /**
         * Used for errors related to COM (Component Object Model) dispatch interfaces.
         */
        FACILITY_DISPATCH(2),
        /**
         * Indicates an error related to data storage, such as file systems, databases, or other data storage mechanisms.
         */
        FACILITY_STORAGE (3),
        /**
         * Used for errors originating from an interface-based system, like COM interfaces or other interface-related services.
         */
        FACILITY_ITF(4),
        /**
         * Standard Win32 error codes.
         */
        FACILITY_WIN32(7),
        /**
         * General Windows error.
         */
        FACILITY_WINDOWS(8),
        /**
         * Security-related.
         */
        FACILITY_SECURITY(9),
        /**
         * Windows Control Manager.
         */
        FACILITY_SSPI(10),
        /**
         * Used for system management and control errors.
         */
        FACILITY_CONTROL(11),
        /**
         * Errors related to certificate services, including certificate validation and management.
         */
        FACILITY_CERT(12),
        /**
         * Errors related to HTTP and web-related services.
         */
        FACILITY_HTTP(13),
        /**
         * Indicates an error from user-mode code (applications and processes outside the kernel).
         */
        FACILITY_USERMODE(14),
        /**
         * Direct3D errors.
         */
        FACILITY_DIRECT3D(17),
        /**
         * Errors related to the Active Accessibility Framework.
         */
        FACILITY_AAF(19),
        /**
         * General DirectX errors.
         */
        FACILITY_DIRECTX(20),
        /**
         * Errors originating from the .NET runtime.
         */
        FACILITY_URT (21),
        /**
         * Errors related to Microsoft Message Queuing (MSMQ).
         */
        FACILITY_MSMQ (32);

        final int facilityValue;
        HResultFacility(int facilityValue) {
            this.facilityValue = facilityValue;
        }

        static HResultFacility valueOf(final int facilityValue) {
            for (HResultFacility facility : HResultFacility.values()) {
                if (facility.facilityValue == facilityValue) { return facility; }
            }
            return FACILITY_NULL;
        }
    }

    public enum HResultCode {
        ERROR_INVALID_PARAMETER(87), ERROR_ACCESS_DENIED(5), ERROR_FILE_NOT_FOUND(2);
        final int codeValue;
        HResultCode(int codeValue) {
            this.codeValue = codeValue;
        }

        static HResultCode valueOf(final int codeValue) {
            for (HResultCode code : HResultCode.values()) {
                if (code.codeValue == codeValue) { return code; }
            }
            return ERROR_INVALID_PARAMETER;
        }
    }

    @Override
    public String toString() {
        return "HResultException{" +
               "hresult=" + hresult +
               ", severity=" + severity +
               ", facility=" + facility +
               ", code=" + code +  ", message=" + getMessage() +
               '}';
    }
}
