function ManageContestantsViewModel() {
	var self = this;

	self.polls = ko.observableArray();
	self.selectedPoll = ko.observable();
	self.selectedPollData = ko.observableArray();
	
	self.ready = ko.observable(false);
	self.contestantCollectionReady = ko.observable(false);
	
	self.imageLocation = "/Sweethearts-Poll/contestantImg/";
	
	self.selectedPartner = ko.observable();

	// Behaviours
	self.selectPoll = function(poll) {
		self.contestantCollectionReady(false);
		console.log("selected Poll: " + JSON.stringify(poll));
		self.selectedPoll(poll);
		$.post("/Sweethearts-Poll/ContestantProvider", {
			request : 1,
			obj : JSON.stringify(poll)
		}).done(function(data) {
			self.selectedPollData(data);
			console.log("Contestants for Poll: " + JSON.stringify(data));
			self.contestantCollectionReady(true);
			console.log("Visible after contestants: " + self.ready());
			//console.log("Loading contestant: " + JSON.stringify(self.selectedPollData[0]));
			//self.selectContestant(data[0]);
		}).fail(function(error) {
			alert(JSON.stringify(error));
		});
	};

	self.fullName = function(contestant) {
		return contestant.firstName + " " + contestant.lastName;
	};
	
	self.fullPictureLocation = function(contestant) {
		return self.imageLocation + contestant.pictureFileName;
	};
	
	self.toggleReady = function() {
		self.ready(!self.ready());
		console.log("Ready = " + self.ready());
	};

	// Data Pull
	$.ajax({
		url : '/Sweethearts-Poll/PollProvider',
		dataType : "json",
		method : 'POST',
		success : function(data) {
			console.log("Data received:" + JSON.stringify(data));
			self.polls(data);
			self.ready(true);
			self.selectPoll(self.polls()[0]);
		},
		failure : function(error) {
			console.log(error);
		}
	});
}

$(document).ready(function(){
    $('.collapsible').collapsible({
      accordion : true // A setting that changes the collapsible behavior to expandable instead of the default accordion style
    });
  });

ko.applyBindings(new ManageContestantsViewModel());
