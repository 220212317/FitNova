package za.ac.cput.entity;

/**
 *
 * 25 March 2026
 * Author: Lisakhanya Tshokolo
 * (220239215)
 */

public class Member {
private String memberId;
private UserAccount account;
private UserProfile profile;
private ContactDetails contact;

private Member(Builder builder) {
    this.memberId = builder.memberId;
    this.account = builder.account;
    this.profile = builder.profile;
    this.contact = builder.contact;

}

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public UserAccount getAccount() {
        return account;
    }

    public void setAccount(UserAccount account) {
        this.account = account;
    }

    public UserProfile getProfile() {
        return profile;
    }

    public void setProfile(UserProfile profile) {
        this.profile = profile;
    }

    public ContactDetails getContact() {
        return contact;
    }

    public void setContact(ContactDetails contact) {
        this.contact = contact;
    }

    public static class Builder {
        private String memberId;
        private UserAccount account;
        private UserProfile profile;
        private ContactDetails contact;
        public Builder memberId(String memberId) {
            this.memberId = memberId;
            return this;
        }
        public Builder account(UserAccount account) {
            this.account = account;
            return this;
        }
        public Builder profile(UserProfile profile) {
            this.profile = profile;
            return this;
        }
        public Builder contact(ContactDetails contact) {
            this.contact = contact;
            return this;
        }

        @Override
        public String toString() {
            return "Builder{" +
                    "memberId='" + memberId + '\'' +
                    ", account=" + account +
                    ", profile=" + profile +
                    ", contact=" + contact +
                    '}';
        }

        public Member build() {
            return new Member(this);
        }
    }
}
