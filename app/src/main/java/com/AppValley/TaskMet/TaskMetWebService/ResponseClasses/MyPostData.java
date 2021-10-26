package com.AppValley.TaskMet.TaskMetWebService.ResponseClasses;

public class MyPostData {

    //Common parameters for all categories

    int id;
    String key;
    String title;
    String mainCategory;
    String currentDate;
    String expireDate;
    int isShowLocation;
    int isShowNumber;
    int isPremiumPost;
    int isApproved;
    int isRejected;
    int isClosed;
    int likes;
    int views;
    int isWaiting;
    int isComplete;
    String description;

    String link;

    //shopping

    String subCategory;
    String condition;
    String condition_meter;
    String price;

    //Job_service

    String type;
    String jobCategory;
    String positionType;
    String salaryFrom;
    String salaryTo;
    String salaryPeriod;
    String expectedSalary;
    String companyName;

    //marriage_service

    String lookingFor;
    String maritialStatus;
    String myHeight;
    String myAge;
    String myReligion;
    String myCastClan;
    String myEducation;
    String myJob;
    String mySalary;
    String writeMyself;
    String partnerHeight;
    String partnerAge;
    String partnerReligion;
    String partnerCastClan;
    String partnerEducation;
    String partnerJob;
    String aboutPartner;

    //common Service
    String cost;


    //------------------------------------ Getter & Setters ----------------------------------------


    public int getPost_id() {
        return id;
    }

    public void setPost_id(int id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMainCategory() {
        return mainCategory;
    }

    public void setMainCategory(String mainCategory) {
        this.mainCategory = mainCategory;
    }

    public int isShowLocation() {
        return isShowLocation;
    }

    public void setShowLocation(int showLocation) {
        isShowLocation = showLocation;
    }

    public int isShowNumber() {
        return isShowNumber;
    }

    public void setShowNumber(int showNumber) {
        isShowNumber = showNumber;
    }

    public int isPremiumPost() {
        return isPremiumPost;
    }

    public void setPremiumPost(int premiumPost) {
        isPremiumPost = premiumPost;
    }

    public int isApproved() {
        return isApproved;
    }

    public void setApproved(int approved) {
        isApproved = approved;
    }

    public int isRejected() {
        return isRejected;
    }

    public void setRejected(int rejected) {
        isRejected = rejected;
    }

    public int isClosed() {
        return isClosed;
    }

    public void setClosed(int closed) {
        isClosed = closed;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getViews() {
        return views;
    }

    public int getIsShowLocation() {
        return isShowLocation;
    }

    public void setIsShowLocation(int isShowLocation) {
        this.isShowLocation = isShowLocation;
    }

    public int getIsShowNumber() {
        return isShowNumber;
    }

    public void setIsShowNumber(int isShowNumber) {
        this.isShowNumber = isShowNumber;
    }

    public int getIsPremiumPost() {
        return isPremiumPost;
    }

    public void setIsPremiumPost(int isPremiumPost) {
        this.isPremiumPost = isPremiumPost;
    }

    public int getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(int isApproved) {
        this.isApproved = isApproved;
    }

    public int getIsRejected() {
        return isRejected;
    }

    public void setIsRejected(int isRejected) {
        this.isRejected = isRejected;
    }

    public int getIsClosed() {
        return isClosed;
    }

    public void setIsClosed(int isClosed) {
        this.isClosed = isClosed;
    }

    public int getIsWaiting() {
        return isWaiting;
    }

    public void setIsWaiting(int isWaiting) {
        this.isWaiting = isWaiting;
    }

    public int getIsComplete() {
        return isComplete;
    }

    public void setIsComplete(int isComplete) {
        this.isComplete = isComplete;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int isWaiting() {
        return isWaiting;
    }

    public void setWaiting(int waiting) {
        isWaiting = waiting;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getCondition_meter() {
        return condition_meter;
    }

    public void setCondition_meter(String condition_meter) {
        this.condition_meter = condition_meter;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getJobCategory() {
        return jobCategory;
    }

    public void setJobCategory(String jobCategory) {
        this.jobCategory = jobCategory;
    }

    public String getPositionType() {
        return positionType;
    }

    public void setPositionType(String positionType) {
        this.positionType = positionType;
    }

    public String getSalaryFrom() {
        return salaryFrom;
    }

    public void setSalaryFrom(String salaryFrom) {
        this.salaryFrom = salaryFrom;
    }

    public String getSalaryTo() {
        return salaryTo;
    }

    public void setSalaryTo(String salaryTo) {
        this.salaryTo = salaryTo;
    }

    public String getSalaryPeriod() {
        return salaryPeriod;
    }

    public void setSalaryPeriod(String salaryPeriod) {
        this.salaryPeriod = salaryPeriod;
    }

    public String getExpectedSalary() {
        return expectedSalary;
    }

    public void setExpectedSalary(String expectedSalary) {
        this.expectedSalary = expectedSalary;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLookingFor() {
        return lookingFor;
    }

    public void setLookingFor(String lookingFor) {
        this.lookingFor = lookingFor;
    }

    public String getMaritialStatus() {
        return maritialStatus;
    }

    public void setMaritialStatus(String maritialStatus) {
        this.maritialStatus = maritialStatus;
    }

    public String getMyHeight() {
        return myHeight;
    }

    public void setMyHeight(String myHeight) {
        this.myHeight = myHeight;
    }

    public String getMyAge() {
        return myAge;
    }

    public void setMyAge(String myAge) {
        this.myAge = myAge;
    }

    public String getMyReligion() {
        return myReligion;
    }

    public void setMyReligion(String myReligion) {
        this.myReligion = myReligion;
    }

    public String getMyCastClan() {
        return myCastClan;
    }

    public void setMyCastClan(String myCastClan) {
        this.myCastClan = myCastClan;
    }

    public String getMyEducation() {
        return myEducation;
    }

    public void setMyEducation(String myEducation) {
        this.myEducation = myEducation;
    }

    public String getMyJob() {
        return myJob;
    }

    public void setMyJob(String myJob) {
        this.myJob = myJob;
    }

    public String getMySalary() {
        return mySalary;
    }

    public void setMySalary(String mySalary) {
        this.mySalary = mySalary;
    }

    public String getWriteMyself() {
        return writeMyself;
    }

    public void setWriteMyself(String writeMyself) {
        this.writeMyself = writeMyself;
    }

    public String getPartnerHeight() {
        return partnerHeight;
    }

    public void setPartnerHeight(String partnerHeight) {
        this.partnerHeight = partnerHeight;
    }

    public String getPartnerAge() {
        return partnerAge;
    }

    public void setPartnerAge(String partnerAge) {
        this.partnerAge = partnerAge;
    }

    public String getPartnerReligion() {
        return partnerReligion;
    }

    public void setPartnerReligion(String partnerReligion) {
        this.partnerReligion = partnerReligion;
    }

    public String getPartnerCastClan() {
        return partnerCastClan;
    }

    public void setPartnerCastClan(String partnerCastClan) {
        this.partnerCastClan = partnerCastClan;
    }

    public String getPartnerEducation() {
        return partnerEducation;
    }

    public void setPartnerEducation(String partnerEducation) {
        this.partnerEducation = partnerEducation;
    }

    public String getPartnerJob() {
        return partnerJob;
    }

    public void setPartnerJob(String partnerJob) {
        this.partnerJob = partnerJob;
    }

    public String getAboutPartner() {
        return aboutPartner;
    }

    public void setAboutPartner(String aboutPartner) {
        this.aboutPartner = aboutPartner;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

}
