/**
 * @author kevint
 */
function ModifyContestantViewModel() {
	var self = this;
	
	self.ready = ko.observable(false);
	
	self.requestedId = ko.observable();
	
	self.contestant = ko.observable();
	
	self.firstName = ko.observable();
	self.lastName = ko.observable();
	self.partner = ko.observable();
	self.pictureFileName = ko.observable();
	
	self.allContestants = ko.observableArray();
	
	self.imageLocation = "/Sweethearts-Poll/contestantImg/";
	
	self.fullName = function(contestant) {
		return contestant.firstName + " " + contestant.lastName;
	};
	
	self.fullPictureLocation = function(contestant) {
		return self.imageLocation + contestant.pictureFileName;
	};
	
	self.getPartnerOptions = function(contestant) {
		var results = ko.observableArray(self.allContestants());
		for(var i = 0; i<results().length; i++) {
			var obj = results()[i];
			if(obj.id == contestant.id) {
				results().splice( i, 1 );;
			}
		}
		//console.log("Kept: " + JSON.stringify(results()));
		return results();
	};
	
	// Behaviours
	self.selectContestants = function(poll) {
		$.post("/Sweethearts-Poll/ContestantProvider", {
			request : 2,
		}).done(function(data) {
			self.allContestants(data);
		}).fail(function(error) {
			alert(JSON.stringify(error));
		});
	};
	
	self.updateContestant = function() {
		self.contestant().firstName = self.firstName();
		self.contestant().lastName = self.lastName();
		self.contestant().voteGroupID = self.partner().voteGroupID;
		self.contestant().pictureFileName = self.pictureFileName();
		
		console.log("Submitting contestant: " + JSON.stringify(contestant));
		
		$.post("/Sweethearts-Poll/ContestantProvider", {
			request : 3,
			obj : JSON.stringify(contestant)
		}).done(function(data) {
			console.log("Submitted new contestant: " + JSON.stringify(data));
		}).fail(function(error) {
			alert(JSON.stringify(error));
		});
	};
	
	// Data Pull
	$.post("/Sweethearts-Poll/ContestantProvider", {
		request : 0,
		id : self.requestedId()
	}).done(function(data) {
		console.log("Submitted new contestant: " + JSON.stringify(data));
	}).fail(function(error) {
		alert(JSON.stringify(error));
	});
}

ko.applyBindings(new ModifyContestantViewModel());
